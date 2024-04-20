package org.example;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.TypeDescriptors;

public class JsonParsing2 {
    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(options);

        // Read input JSON file
        PCollection<String> input = pipeline.apply(TextIO.read().from("input.json"));

        // Parse JSON and extract first_name, last_name, gender, and country
        PCollection<KV<String, String>> parsedData = input.apply("Parse JSON", ParDo.of(new DoFn<String, KV<String, String>>() {
            @ProcessElement
            public void processElement(ProcessContext c) {
                String line = c.element();
                // Parse JSON and extract fields
                // Here, you might want to use a JSON parsing library like Gson or Jackson
                // For simplicity, let's assume the JSON structure remains consistent
                String[] parts = line.split(",");
                String country = parts[4].split(":")[1].replaceAll("\"", "");
                String gender = parts[6].split(":")[1].replaceAll("\"", "");
                c.output(KV.of(country + "," + gender, String.valueOf(1))); // Count each occurrence as 1
            }
        }));

        // Count occurrences of each country-gender pair
        PCollection<KV<String, Long>> countryGenderCounts = parsedData.apply(Count.perKey());

        // Format counts as strings
        PCollection<String> formattedCounts = countryGenderCounts.apply("Format counts",
                MapElements.into(TypeDescriptors.strings()).via(kv -> {
                    String[] parts = kv.getKey().split(",");
                    return parts[0] + "," + parts[1] + "," + kv.getValue();
                }));

        // Write output to CSV file
        formattedCounts.apply("Write CSV", TextIO.write().to("output.csv").withFooter("Country,Gender,Count").withNumShards(1).withSuffix(".csv"));

        pipeline.run().waitUntilFinish();
    }
}
