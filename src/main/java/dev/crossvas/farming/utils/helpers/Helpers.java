package dev.crossvas.farming.utils.helpers;

import net.minecraftforge.common.util.LazyOptional;

import java.util.Map;
import java.util.function.Function;

public class Helpers {

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int size) {
        return isMouseOver(mouseX, mouseY, x, y, size, size);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }

    /**
     * A replacement wrapper for {@link Map#computeIfAbsent(Object, Function)}
     * that can handle a {@link LazyOptional} being invalidated.
     * @param map             A mapping between a generic key and a value wrapped in a
     *                        LazyOptional.
     * @param key             The key to test for.
     * @param mappingFunction The mapping function to execute if the value
     *                        is missing or invalidated. This function should probably
     *                        not return null, instead it should probably return
     *                        {@link LazyOptional#empty}.
     * @param <K>             The generic key type.
     * @return The value associated with the key (either pre-existing, or
     * newly created if the value was previously missing or
     * invalidated) wrapped in a LazyOptional. This can be null, if
     * the mapping function returns a null, though it shouldn't.
     */
    public static <K> LazyOptional<?> computeIfLazyAbsent(Map<K, LazyOptional<?>> map, K key, Function<? super K, ? extends LazyOptional<?>> mappingFunction){
        // If the value is fully missing, defer to the original functionality of Map.
        if(!map.containsKey(key)){
            return map.computeIfAbsent(key, mappingFunction);
        }

        LazyOptional<?> value = map.get(key);

        // If the value is null, defer to the original functionality of Map.
        if(value == null){
            return map.computeIfAbsent(key, mappingFunction);
        }

        // If the value is present, there is no need to perform the mapping.
        if(value.isPresent()){
            return value;
        }

        // Create the new value.
        value = mappingFunction.apply(key);

        // If the value is not null (which should always be true), store it into the map.
        if(value != null){
            map.put(key, value);
        }

        return value;
    }
}
