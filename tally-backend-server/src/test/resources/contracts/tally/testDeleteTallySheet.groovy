package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name("deleteExistingTallySheet")
    request {
        method DELETE()
        url '/admin/adminKey'
        headers {}
    }
    response {
        status OK()
        headers {}
    }
}
