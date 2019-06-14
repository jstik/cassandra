package com.jstik.site.cassandra.convertors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    private static class StringToEnumConverter<T extends Enum> implements Converter<String, T> {

        private Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            String normalize = normalize(source.trim());
            try{
                return (T) Enum.valueOf(this.enumType, normalize);
            }catch (IllegalArgumentException e){
                return (T) tryIgnoreCase(normalize);

            }

        }

        private static String normalize(String source){
            return source.trim().replaceAll("-", "_");
        }

        private Enum tryIgnoreCase(String source) {
            try {
                return Enum.valueOf(this.enumType, source.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Enum.valueOf(this.enumType, source.toLowerCase());
            }
        }
    }

    @Override
    public <T extends Enum> Converter<String, T> getConverter(
            Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }
}