package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("deleteExistingTallySheet")
    request {
        method DELETE()
        url '/adminKey'
        headers {}
    }
    response {
        status OK()
        headers {}
    }
}
