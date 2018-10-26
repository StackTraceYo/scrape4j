package org.stacktrace.yo.crawler.core.pipeline;


import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Supplier;

public class Pipeline {


    private String start;
    private List<Step> steps = Lists.newArrayList();


    public Step start(String url) {
        this.start = url;
        return new Step(this, () -> url);
    }


    public static final class Step {

        private Pipeline pipeline;
        private List<String> xpaths = Lists.newArrayList();
        private Supplier<String> url;

        public Step(Pipeline builder, Supplier<String> url) {
            this.pipeline = builder;
            this.url = url;
        }

        public Step extract(String xpath) {
            xpaths.add(xpath);
            return this;
        }

        public Step findAndThen(String xpath, Supplier<String> continueTo) {
            xpaths.add(xpath);
            pipeline.steps.add(this);
            return new Step(pipeline, continueTo);
        }

        public Step extractAndThen(String xpath, Supplier<String> continueTo) {
            xpaths.add(xpath);
            pipeline.steps.add(this);
            return new Step(pipeline, continueTo);
        }

        public Pipeline finish() {
            pipeline.steps.add(this);
            return pipeline;
        }


    }


}
