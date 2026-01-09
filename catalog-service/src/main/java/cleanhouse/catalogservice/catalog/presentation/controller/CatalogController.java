package cleanhouse.catalogservice.catalog.presentation.controller;

import cleanhouse.catalogservice.catalog.application.dto.CatalogListResponse;
import cleanhouse.catalogservice.catalog.application.port.CatalogQueryUsecase;
import cleanhouse.catalogservice.catalog.application.query.GetCatalogsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogQueryUsecase catalogQueryUsecase;

    @GetMapping("/catalogs")
    public ResponseEntity<CatalogListResponse> getCatalogs() {
        log.info("Fetching all catalogs");
        GetCatalogsQuery query = new GetCatalogsQuery();
        CatalogListResponse response = catalogQueryUsecase.getCatalogs(query);
        return ResponseEntity.ok(response);
    }
}
