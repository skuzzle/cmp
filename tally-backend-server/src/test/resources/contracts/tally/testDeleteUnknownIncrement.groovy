package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("deleteUnknownIncrement")
    request {
        method DELETE()
        url '/adminKey/increment/unknownIncrementId'
        headers {}
    }
    response {
        status NOT_FOUND()
        headers {}
        body([
            message: "unknownIncrementId",
            origin: "de.skuzzle.tally.service.IncrementNotAvailableException"
        ])
    }
}
