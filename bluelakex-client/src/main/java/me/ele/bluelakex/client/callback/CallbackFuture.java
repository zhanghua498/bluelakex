package me.ele.bluelakex.client.callback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CallbackFuture<V> implements Future<V> {
	private boolean canceled = false;
	private boolean done = false;
	private V value;
	private ReentrantLock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		canceled = true;
		return false;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		lock.lock();
		try {
			if (!done) {
				cond.await();
			}
		} finally {
			lock.unlock();
		}
		return value;
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		lock.lock();
		try {
			if (!done) {
				boolean success = cond.await(timeout, unit);
				if (!success) {
					throw new TimeoutException();
				}
			}
		} finally {
			lock.unlock();
		}
		return value;
	}

	@Override
	public boolean isCancelled() {
		return canceled;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	public void done(V value) {
		lock.lock();
		try {
			if (!done) {
				this.value = value;
				this.done = true;
				cond.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}
}
