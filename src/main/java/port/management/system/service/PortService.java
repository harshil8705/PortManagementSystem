package port.management.system.service;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import port.management.system.model.Port;
import port.management.system.dto.*;

import java.io.IOException;

public interface PortService {

    PortDTO addNewPortByCountryId(Long countryId, @Valid Port port);

    PortDTO updatePortById(Long portId, @Valid PortDTO2 updatedPortDTO2);

    PortResponse getAllPortsByCountryId(Long countryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PortResponse getAllPorts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PortResponse getAllPortsByName(String portName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PortDTO getPortById(Long portId);

    String deletePortById(Long portId);

    PortAnalysisDTO getPortAnalysisById(Long portId);

    PortDTO uploadPortImage(Long portId, MultipartFile image) throws IOException;

}
