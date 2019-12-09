package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("getUnknownTallySheet")
    request {
        method GET()
        url '/unknownPublicKey'
        headers {}
    }
    response {
        status NOT_FOUND()
        headers {}
        body([
            message: "unknownPublicKey",
            origin: "de.skuzzle.cmp.counter.TallySheetNotAvailableException"
        ])
    }
}
