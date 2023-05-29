package com.circle.paquan.bus;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.ConcurrentHashMap;

public class LiveDataBus {

    private static class Lazy {
        static LiveDataBus sLiveDataBus = new LiveDataBus();
    }

    public static LiveDataBus get() {
        return Lazy.sLiveDataBus;
    }

    private static final ConcurrentHashMap<String, StickyLiveData> mHashMap = new ConcurrentHashMap<>();

    public StickyLiveData with(String eventName) {
        StickyLiveData liveData = mHashMap.get(eventName);
        if (liveData == null) {
            liveData = new StickyLiveData(eventName);
            mHashMap.put(eventName, liveData);
        }
        return liveData;
    }

    /**
     * 实际上liveData黏性事件总线的实现方式 还有另外一套实现方式。
     * 一堆反射 获取LiveData的mVersion字段，来控制数据的分发与否，不够优雅。
     * <p>
     * 但实际上 是不需要那么干的。请看我们下面的实现方式。
     *
     * @param <T>
     */
    public static class StickyLiveData<T> extends LiveData<T> {

        private String mEventName;

        private T mStickyData;

        private int mVersion = 0;

        public StickyLiveData(String eventName) {

            mEventName = eventName;
        }

        @Override
        public void setValue(T value) {
            mVersion++;
            super.setValue(value);
        }

        @Override
        public void postValue(T value) {
            mVersion++;
            super.postValue(value);
        }

        public void setStickyData(T stickyData) {
            this.mStickyData = stickyData;
            setValue(stickyData);
        }

        public void postStickyData(T stickyData) {
            this.mStickyData = stickyData;
            postValue(stickyData);
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            observerSticky(owner, observer, false);
        }

        public void observerSticky(LifecycleOwner owner, Observer<? super T> observer, boolean sticky) {
            super.observe(owner, new WrapperObserver(this, observer, sticky));
            owner.getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        mHashMap.remove(mEventName);
                    }
                }
            });
        }


        private class WrapperObserver<T> implements Observer<T> {
            private StickyLiveData<T> mLiveData;
            private Observer<T> mObserver;
            private boolean mSticky;

            //标记该liveData已经发射几次数据了，用以过滤老数据重复接收
            private int mLastVersion = 0;

            public WrapperObserver(StickyLiveData liveData, Observer<T> observer, boolean sticky) {


                mLiveData = liveData;
                mObserver = observer;
                mSticky = sticky;

                mLastVersion = mLiveData.mVersion;
            }

            @Override
            public void onChanged(T t) {
                if (mLastVersion >= mLiveData.mVersion) {
                    //但如果当前observer它是关心 黏性事件的，则给他。
                    if (mSticky && mLiveData.mStickyData != null) {
                        mObserver.onChanged(mLiveData.mStickyData);
                    }
                    return;
                }

                mLastVersion = mLiveData.mVersion;
                mObserver.onChanged(t);
            }
        }
    }
}
