package org.ronald.learn.retrofit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.operators.observable.ObservableScalarXMap.ScalarDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Penoder
 * @date 2018/9/5
 */
public class BaseObservable<T> extends Observable<T> implements ScalarCallable {

    private final T value;

    public BaseObservable(final T value) {
        this.value = value;
    }

    @Override
    protected void subscribeActual(Observer<? super T> s) {
        ScalarDisposable<T> sd = new ScalarDisposable<>(s, value);
        s.onSubscribe(sd);
        sd.run();
    }

    @Override
    public T call() {
        return value;
    }


    public void execute(Consumer consumer) {
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }
}