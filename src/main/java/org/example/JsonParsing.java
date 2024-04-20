package org.example;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.TypeDescriptors;

public class JsonParsing {
    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(options);

        // Read input JSON file
        PCollection<String> input = pipeline.apply(TextIO.read().from("input.json"));

        // Parse JSON and extract first_name, last_name, and country
        PCollection<KV<String, String>> parsedData = input.apply("Parse JSON", ParDo.of(new DoFn<String, KV<String, String>>() {
            @ProcessElement
            public void processElement(ProcessContext c) {
                String line = c.element();
                // Parse JSON and extract fields
                // Here, you might want to use a JSON parsing library like Gson or Jackson
                // For simplicity, let's assume the JSON structure remains consistent
                String[] parts = line.split(",");
                String first_name = parts[0].split(":")[1].replaceAll("\"", "");
                String last_name = parts[1].split(":")[1].replaceAll("\"", "");
                String country = parts[4].split(":")[1].replaceAll("\"", "");
                c.output(KV.of(country, first_name + "," + last_name));
            }
        }));

        // Count number of people in each country
        PCollection<KV<String, Long>> countryCounts = parsedData.apply(Count.perKey());

        // Format country counts as strings
        PCollection<String> formattedCounts = countryCounts.apply("Format counts", MapElements.into(TypeDescriptors.strings()).via(kv -> kv.getKey() + "," + kv.getValue()));

        // Write output to CSV file
        formattedCounts.apply("Write CSV", TextIO.write().to("output.csv").withFooter("Country,Count").withNumShards(1).withSuffix(".csv"));

        pipeline.run().waitUntilFinish();
    }
}
