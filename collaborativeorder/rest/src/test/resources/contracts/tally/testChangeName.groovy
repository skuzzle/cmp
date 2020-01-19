package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("changeName")
    request {
        method PUT()
        url '/adminKey2/changeName/newName'
    }
    response {
        status OK()
        headers {}
    }
}
