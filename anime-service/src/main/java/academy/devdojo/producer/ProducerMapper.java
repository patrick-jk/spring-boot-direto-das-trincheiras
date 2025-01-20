package academy.devdojo.producer;

import academy.devdojo.domain.Producer;
import academy.devdojo.dto.ProducerGetResponse;
import academy.devdojo.dto.ProducerPostRequest;
import academy.devdojo.dto.ProducerPostResponse;
import academy.devdojo.dto.ProducerPutRequest;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

  Producer toProducer(ProducerPostRequest postRequest);

  Producer toProducer(ProducerPutRequest request);

  ProducerGetResponse toProducerGetResponse(Producer producer);

  ProducerPostResponse toProducerPostResponse(Producer producer);

  List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);
}