/*
 * Copyright (c) 2011-2018 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.core.publisher;

import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;

/**
 * A symmetric {@link Processor} that can be converted to a {@link Mono} or {@link Flux}.
 * <p>
 * This is a facade over the much more complex API of {@link MonoProcessor}.
 *
 * @author Simon Baslé
 */
public interface SimpleMonoProcessor<T> extends Processor<T, T> {

	/**
	 * Converts the {@link SimpleMonoProcessor} to a {@link Flux}, allowing to compose
	 * operators on the downstream side of the {@link Processor}.
	 *
	 * @return the {@link SimpleMonoProcessor} viewed as a {@link Flux}
	 */
	Flux<T> toFlux();

	/**
	 * Converts the {@link SimpleMonoProcessor} to a {@link Mono}, allowing to compose
	 * operators on the downstream side of the {@link Processor}.
	 * <p>
	 * When possible, if the concrete implementation already is derived from {@link Mono}
	 * this method should not instantiate any intermediate object.
	 *
	 * @return the {@link SimpleMonoProcessor} viewed as a {@link Mono}
	 */
	Mono<T> toMono();

	/**
	 * @return the {@link Processor} backing this {@link SimpleMonoProcessor}
	 */
	Processor<T, T> toMonoProcessor(); //FIXME switch to a common root interface for various mono processors implementations (when we have several)



	//== Factory methods ==

	/**
	 * Create a new "first" {@link SimpleMonoProcessor}, which can be later subscribed to a
	 * {@link Publisher} source, of which it will replay the first element to all its current
	 * and future subscribers.
	 *
	 * @param <O> the data type
	 * @return a new "first" {@link SimpleMonoProcessor}
	 */
	static <O> SimpleMonoProcessor<O> first() {
		return MonoProcessor.create();
	}

	/**
	 * Create a new "first" {@link SimpleMonoProcessor} that is directly connected to a
	 * {@link Publisher} source, of which it will replay the first element to all its
	 * future subscribers.
	 *
	 * @param <O> the data type
	 * @return a new "first" {@link SimpleMonoProcessor}
	 */
	static <O> SimpleMonoProcessor<O> firstOf(Publisher<? extends O> source) {
		MonoProcessor<O> processor = new MonoProcessor<>(source);
		processor.connect();
		return processor;
	}

}
