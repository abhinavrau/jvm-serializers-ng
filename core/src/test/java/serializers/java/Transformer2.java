package serializers.java;

import org.mapstruct.Mapper;

@Mapper
public interface Transformer2<A, B> {

  B forward(A a);

  A reverse(B a);

}
