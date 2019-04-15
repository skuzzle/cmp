package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name("getUnknownTallySheet")
    request {
        method GET()
        url '/public/unknownPublicKey'
        headers {}
    }
    response {
        status FORBIDDEN()
        headers {}
    }
}
