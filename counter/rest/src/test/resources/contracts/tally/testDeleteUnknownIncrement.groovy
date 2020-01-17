package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("deleteUnknownIncrement")
    request {
        method DELETE()
        url '/adminKey1/increment/unknownIncrementId'
        headers {}
    }
    response {
        status NOT_FOUND()
        headers {}
        body([
            message: "unknownIncrementId",
            origin: "IncrementNotAvailableException"
        ])
    }
}
