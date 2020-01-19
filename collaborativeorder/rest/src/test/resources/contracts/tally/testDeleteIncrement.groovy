package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("deleteIncrement")
    request {
        method DELETE()
        url '/adminKey2/increment/incrementId'
    }
    response {
        status OK()
        headers {}
    }
}
