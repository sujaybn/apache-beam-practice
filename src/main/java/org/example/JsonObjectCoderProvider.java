package org.example;

import org.apache.beam.sdk.coders.CannotProvideCoderException;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.values.TypeDescriptor;

public interface JsonObjectCoderProvider {
    <T> Coder<T> getCoder(TypeDescriptor<T> typeDescriptor) throws CannotProvideCoderException;
}
