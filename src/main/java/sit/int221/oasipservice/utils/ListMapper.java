package sit.int221.oasipservice.utils;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ListMapper {
    private static final ListMapper listMapper = new ListMapper();

    private ListMapper() {
    }

    public <S, T> List<T> mapList(List<S> src, Class<T> targetClass, ModelMapper modelMapper) {
        return src.stream().map(entity -> modelMapper.map(entity, targetClass))
                .collect(Collectors.toList());
    }

    public static ListMapper getInstance() {
        return listMapper;
    }
}
