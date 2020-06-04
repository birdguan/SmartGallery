package com.birdguan.smartgallery.impl;

import android.provider.Telephony;

import com.birdguan.smartgallery.base.Chain;
import com.birdguan.smartgallery.base.util.MyLog;

import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: birdguan
 * @Date: 2020/6/4 11:41
 */
public abstract class ConsumerChain<T> implements Chain<T, Mat> {
    public static final String TAG = "SmartGallery: ConsumerChain";

    private List<BaseMyConsumer> mConsumerList; // 存储consumer的列表
    private int mConsumerPointer;               // 指向当前consumer的指针

    private Mat mFirstMat;      // 初始的图像
    private Mat mPreviousMat;   // 当前图像的前一个图像
    private Mat mNowMat;        // 当前的图像
    private Rect nowRect;

    private boolean isStarted;  // 同一个Chain可以重新调用restart启动
    private boolean isInited;   // 同一个Chain只能初始化一次

    protected ConsumerChain() {
        this.mConsumerList = new ArrayList<>();
        this.mConsumerPointer = -1;
        this.mFirstMat = null;
        this.mPreviousMat = null;
        this.mNowMat = null;
        this.isStarted = false;
        this.isInited = false;
        MyLog.d(TAG, "ConsumerChain", "状态:mConsumerPoint",
                "初始化", mConsumerPointer);
    }

    /**
     * 设置第一个图像，一个对象只能被初始化一次
     * @param startParam
     */
    public void init(@NotNull T startParam) {
        MyLog.d(TAG, "init", "状态:startParam:",
                "初始化", startParam);
        if (isInited) {
            throw new RuntimeException("该Chain已经初始化了，不能再重新初始化");
        }
        mFirstMat = getStartResult(startParam);
        mPreviousMat = null;
        mNowMat = mFirstMat;
        isInited = true;
        nowRect = new Rect(0, 0, mFirstMat.width(), mFirstMat.height());
    }

    /**
     * 运行当前传入的consumer，需要在初始化完毕之后调用，一个对象只能运行一次
     * @param consumers
     * @return
     */
    public Mat runStart(@NotNull BaseMyConsumer... consumers) {
        MyLog.d(TAG, "runStart", "状态:consumers:",
                "开始运行", Arrays.toString(consumers));
        if (!isInited) {
            throw new RuntimeException("该chain还未初始化");
        }
        if (isStarted) {
            throw  new RuntimeException("该Chain已经启动");
        }
        if (consumers.length == 0) {
            MyLog.d(TAG, "runStart", "状态:", "传入的consumer为空");
            return mNowMat;
        }
        if (mPreviousMat != null && mPreviousMat != mFirstMat) {
            mPreviousMat.release();
        }
        mPreviousMat = mNowMat;
        mNowMat = runConsumers(Arrays.asList(consumers), mFirstMat);
        addConsumers(consumers);

        isStarted = true;
        return mNowMat;
    }

    /**
     * 初始化并开始运行后，继续运行单个consumer，传入的consumer可为null，这样会返回当前的图像
     * @param nextMyConsumer
     * @return
     */
    public Mat runNext(BaseMyConsumer nextMyConsumer) {
        MyLog.d(TAG, "runStart", "状态:consumer", nextMyConsumer);
        checkState();

        if (nextMyConsumer == null) {
            MyLog.d(TAG, "runNext", "状态:", "传入的consumer为空");
            return mNowMat;
        }

        BaseMyConsumer nowConsumer = getNowConsumer();
        boolean isNeedRun = nowConsumer.isNeedRun(nextMyConsumer);
        if (isNeedRun) {
            if (mPreviousMat != null && mPreviousMat != mFirstMat) {
                mPreviousMat.release();
            }
            mPreviousMat = mNowMat;
            mNowMat = runConsumers(Collections.singletonList(nextMyConsumer), mNowMat);
            addConsumer(nextMyConsumer);
        }

        MyLog.d(TAG, "runNext", "状态:isNeedRun:nowConsumer:mNowMat:mPreviousMat:",
                "", isNeedRun, nowConsumer, mNowMat, mPreviousMat);
        return mNowMat;
    }

    /**
     * 修改当前consumer的参数，然后重新运行一遍，这里不会增加consumer的数量，不能修改第一个consumer的参数
     * @param changeMyConsumer
     * @return
     */
    public Mat runNow(BaseMyConsumer changeMyConsumer) {
        MyLog.d(TAG, "runNow", "状态:consumer:",
                "重新运行当前的", changeMyConsumer);
        if (!canRunNow(changeMyConsumer)) {
            throw new RuntimeException("不能重新运行当前consumer");
        }

        BaseMyConsumer nowConsumer = getNowConsumer();
        boolean isNeedRun = nowConsumer.isNeedRun(changeMyConsumer);
        if (isNeedRun) {
            nowConsumer.copy(changeMyConsumer);
            mNowMat = runConsumers(Collections.singletonList(nowConsumer), mPreviousMat);
        }
        MyLog.d(TAG, "runNow",
                "状态:isNeedRun:nowConsumer:changeMyConsumer:mNowMat:mPreviousMat:",
                "", isNeedRun, nowConsumer, changeMyConsumer, mNowMat, mPreviousMat);
        return mNowMat;
    }

    /**
     * 撤销当前consumer对图片的影响，
     * 具体的方法是每次undo都将前面的consumer运行一遍，获取到mPreviousMat，为下次undo做准备
     * @return
     */
    public Mat undo() {
        MyLog.d(TAG, "undo", "状态:mConsumerPointer:currentConsumer:",
                "进入undo", mConsumerPointer, mConsumerList.get(mConsumerPointer));
        checkState();
        if (!canUndo()) {
            throw new RuntimeException("当前状态不可以undo");
        }
        if (mNowMat != null && mNowMat != mFirstMat) {
            mNowMat.release();
        }
        mNowMat = mPreviousMat;
        // 如果当前consumer指针指向第二个consumer，当撤销了之后，mPreviousMat应该为null
        if (mConsumerPointer <= 1) {
            mPreviousMat = null;
        } else if (mConsumerPointer == 2) {
            mPreviousMat = mFirstMat;
        } else {
            int start = 1, end = mConsumerPointer - 1;
            List<BaseMyConsumer> subList = mConsumerList.subList(start, end);
            mPreviousMat = runConsumers(subList, mFirstMat);
            MyLog.d(TAG, "undo", "状态:subList",
                    "当前mConsumerPointer大于2", subList);
        }

        mConsumerPointer--;
        BaseMyConsumer nowConsumer = getNowConsumer();
        if (nowConsumer instanceof CutMyConsumer) {
            nowRect = ((CutMyConsumer) nowConsumer).getRect();
        }

        MyLog.d(TAG, "undo", "状态:mConsumerPointer:mNowMat:mPreviousMat:nowConsumer:",
                "undo完毕", mConsumerPointer, mNowMat, mPreviousMat, nowConsumer);
        return mNowMat;
    }

    public Mat redo() {
        MyLog.d(TAG, "redo", "状态:mConsumerPointer:nextConsumer:",
                "进入redo", mConsumerPointer, mConsumerList.get(mConsumerPointer + 1));
        checkState();
        if (!canRedo()) {
            throw new RuntimeException("当前状态不可以redo");
        }
        mConsumerPointer++;
        if (mPreviousMat != null && mPreviousMat != mFirstMat) {
            mPreviousMat.release();
        }
        mPreviousMat = mNowMat;
        mNowMat = runConsumers(Collections.singletonList(getNowConsumer()), mNowMat);

        MyLog.d(TAG, "redo", "状态:mConsumerPointer:mNowMat:mPreviousMat",
                "redo完成", mConsumerPointer, mNowMat, mPreviousMat);
        return mNowMat;
    }

    /**
     * 取消当前consumer
     * @return
     */
    public Mat cancelNowConsumer() {
        if (!canUndo()) {
            throw new RuntimeException("不可以取消当前的consumer");
        }
        Mat mat = undo();
        removeAfterUndoRedoPointConsumer();
        return mat;
    }

    public boolean canUndo() {
        return mConsumerPointer > 0;
    }

    public boolean canRedo() {
        return mConsumerPointer < mConsumerList.size() - 1;
    }

    public boolean canRunNow(BaseMyConsumer changeMyConsumer) {
        checkState();
        return getNowConsumer().canRunNow(changeMyConsumer);
    }

    public Mat runConsumers(@NotNull List<BaseMyConsumer> consumers, @NotNull Mat mat) {
        MyLog.d(TAG, "runConsumers", "状态:consumers:mat:",
                "运行一些列consumers", consumers, mat);
        Mat nowMat = mat;
        for (int i = 0; i < consumers.size(); i++) {
            BaseMyConsumer consumer = consumers.get(i);

            if (consumer == null) {
                MyLog.d(TAG, "runConsumers", "状态:",
                        "consumer为空，略过该消费者");
                continue;
            }

            if (nowMat == null) {
                consumer.onFailure(new Exception(TAG + ":consumer返回结果为null"));
                nowMat = null;
            } else {
                nowMat = consumer.onNewResult(nowMat);
            }

            if (consumer instanceof CutMyConsumer) {
                nowRect = ((CutMyConsumer) consumer).getRect();
            }
        }
        return nowMat;
    }

    protected void addConsumer(BaseMyConsumer consumer) {
        addConsumers(new BaseMyConsumer[]{consumer});
    }

    /**
     * 先移除consumer指针后面的consumer，然后按顺序将consumer添加到列表中
     * 最后重新将consumer指针指向最后一个consumer
     * @param consumers
     */
    private void addConsumers(@NotNull BaseMyConsumer[] consumers) {
        MyLog.d(TAG, "addConsumers", "状态:consumers:mConsumerPoint:",
                "添加consumer", Arrays.toString(consumers), mConsumerPointer);
        removeAfterUndoRedoPointConsumer();
        for (BaseMyConsumer b : consumers) {
            if (b != null) {
                mConsumerList.add(b);
            }  else {
                MyLog.d(TAG, "addConsumers", "状态:",
                        "被添加的consumer为null，跳过");
            }
        }

        mConsumerPointer = mConsumerList.size() - 1;
        MyLog.d(TAG, "addConsumers", "状态:mConsumerPointer:",
                "添加consumer完毕", mConsumerPointer);
    }

    private void removeAfterUndoRedoPointConsumer() {
        if (mConsumerPointer == mConsumerList.size() - 1) {
            MyLog.d(TAG, "removeAfterUndoRedoPointConsumer", "状态:",
                    "当前不处于undo状态，不需要移除消费者");
            return;
        }
        BaseMyConsumer nowConsumer = getNowConsumer();
        int needRemove = 0;
        for (int i = 0; i < mConsumerList.size(); i++) {
            if (nowConsumer == mConsumerList.get(i)) {
                needRemove = i + 1;
                MyLog.d(TAG, "removeAfterUndoRedoPointConsumer", "状态:needRemove:",
                        "接下来的consumer都需要被remove", needRemove);
                break;
            }
        }

        int needRemoveSize = mConsumerList.size() - needRemove;
        for (int i = 0; i < needRemoveSize; i++) {
            MyLog.d(TAG, "removeAfterUndoRedoPointConsumer", "状态:needRemoveSize:i",
                    "移除一个consumer", needRemoveSize, i);
            BaseMyConsumer removedConsumer = mConsumerList.remove(needRemove);
            removedConsumer.destroy();
        }
    }

    private void checkState() {
        if (!isInited) {
            throw new RuntimeException("该Chain还未初始化");
        }
        if (!isStarted) {
            throw new RuntimeException("该Chain还未启动");
        }
    }

    @Override
    public void destroy() {
        for (BaseMyConsumer removedConsumer : mConsumerList) {
            removedConsumer.destroy();
        }
        mConsumerList.clear();
        mConsumerPointer = -1;
        mFirstMat = null;
        mPreviousMat = null;
        mNowMat = null;
        isStarted = false;
        isInited = false;
        MyLog.d(TAG, "destroy", "状态:mConsumerPointer",
                "销毁", mConsumerPointer);
    }

    public Flowable<Mat> rxRunStartConvenient(BaseMyConsumer... consumers) {
        return rxRunStart(consumers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxRunStart(BaseMyConsumer... consumers) {
        return Flowable.just(consumers)
                .map(this::runStart);
    }

    public Flowable<Mat> rxRunNextConvenient(BaseMyConsumer consumer) {
        return rxRunNext(consumer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Flowable<Mat> rxRunNext(BaseMyConsumer consumer) {
        return Flowable.just(consumer)
                .map(this::runNext);
    }


    public Flowable<Mat> rxRunNowConvenient(BaseMyConsumer consumer) {
        return rxRunNow(consumer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxRunNow(BaseMyConsumer consumer) {
        return Flowable.just(consumer)
                .map(this::runNow);
    }


    public Flowable<Mat> rxUndoConvenient() {
        return rxUndo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxUndo() {
        return Flowable.just("1")
                .map(new Function<Object, Mat>() {
                    @Override
                    public Mat apply(Object o) throws Exception {
                        return undo();
                    }
                });
    }

    public Flowable<Mat> rxRedoConvenient() {
        return rxRedo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxRedo() {
        return Flowable.just("1")
                .map(new Function<String, Mat>() {
                    @Override
                    public Mat apply(String s) throws Exception {
                        return redo();
                    }
                });
    }

    public List<BaseMyConsumer> getConsumerList() {
        return mConsumerList;
    }

    public Rect getNowRect() {
        return nowRect;
    }

    public int getConsumerPoint() {
        return mConsumerPointer;
    }

    public BaseMyConsumer getNowConsumer() {
        checkState();
        return mConsumerList.get(mConsumerPointer);
    }

    public Mat getPreviousMat() {
        // 为了防止被外部释放掉 所以需要进行克隆
        return mPreviousMat == null ? null : mPreviousMat.clone();
    }

    public Mat getFirstMat() {
        // 为了防止被外部释放掉 所以需要进行克隆
        return mFirstMat == null ? null : mFirstMat.clone();
    }

    protected abstract Mat getStartResult(T startParam);
}
