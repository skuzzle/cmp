package contracts.tally


org.springframework.cloud.contract.spec.Contract.make {
    name("getMetaInformation")
    request {
        method GET()
        url '/_meta'
        headers {}
    }
    response {
        status OK()
        body([
            totalTallySheetCount: 3
        ])
        headers {
            contentType(applicationJson())
        }
    }
}
