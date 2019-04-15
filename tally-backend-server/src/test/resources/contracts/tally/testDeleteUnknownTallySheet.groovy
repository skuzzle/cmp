package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name("deleteUnknownTallySheet")
    request {
        method DELETE()
        url '/admin/unknownAdminKey'
        headers {}
    }
    response {
        status FORBIDDEN()
        headers {}
    }
}
