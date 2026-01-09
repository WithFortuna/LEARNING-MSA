package cleanhouse.catalogservice.catalog.application.query;

import cleanhouse.catalogservice.catalog.application.dto.CatalogListResponse;
import cleanhouse.catalogservice.catalog.application.dto.CatalogResponse;
import cleanhouse.catalogservice.catalog.application.port.CatalogQueryUsecase;
import cleanhouse.catalogservice.catalog.domain.entity.Catalog;
import cleanhouse.catalogservice.catalog.domain.port.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogQueryHandler implements CatalogQueryUsecase {
    private final CatalogRepository catalogRepository;

    @Override
    @Transactional(readOnly = true)
    public CatalogListResponse getCatalogs(GetCatalogsQuery query) {
        List<Catalog> catalogs = catalogRepository.findAll();

        List<CatalogResponse> catalogResponses = catalogs.stream()
                .map(CatalogResponse::from)
                .collect(Collectors.toList());

        return new CatalogListResponse(catalogResponses, catalogResponses.size());
    }
}
