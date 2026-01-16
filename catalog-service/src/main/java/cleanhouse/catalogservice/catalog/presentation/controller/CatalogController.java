package cleanhouse.catalogservice.catalog.presentation.controller;

import cleanhouse.catalogservice.catalog.application.command.CreateCatalogCommand;
import cleanhouse.catalogservice.catalog.application.dto.CatalogListResponse;
import cleanhouse.catalogservice.catalog.application.dto.CatalogResponse;
import cleanhouse.catalogservice.catalog.application.dto.CreateCatalogRequest;
import cleanhouse.catalogservice.catalog.application.port.CatalogCommandUsecase;
import cleanhouse.catalogservice.catalog.application.port.CatalogQueryUsecase;
import cleanhouse.catalogservice.catalog.application.query.GetCatalogsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogQueryUsecase catalogQueryUsecase;
    private final CatalogCommandUsecase catalogCommandUsecase;

    @GetMapping("/catalogs")
    public ResponseEntity<CatalogListResponse> getCatalogs() {
        log.info("Fetching all catalogs");
        GetCatalogsQuery query = new GetCatalogsQuery();
        CatalogListResponse response = catalogQueryUsecase.getCatalogs(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/catalogs")
    public ResponseEntity<CatalogResponse> createCatalog(@RequestBody CreateCatalogRequest request) {
        log.info("Creating catalog with productName={}", request.getProductName());
        CreateCatalogCommand command = new CreateCatalogCommand(
                request.getProductName(),
                request.getStock(),
                request.getUnitPrice()
        );
        CatalogResponse response = catalogCommandUsecase.createCatalog(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
