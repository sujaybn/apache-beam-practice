package org.example;

import org.apache.beam.sdk.values.KV;

public interface CountFemalesAndMales {
    org.apache.beam.sdk.coders.Coder<KV<String, Long>> getOutputCoder();
}
