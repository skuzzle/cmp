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
            error: [
                message: "unknownPublicKey",
                origin: "de.skuzzle.tally.service.TallySheetNotAvailableException"
            ]
        ])
    }
}
