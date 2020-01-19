package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("deleteUnknownTallySheet")
    request {
        method DELETE()
        url '/unknownAdminKey'
        headers {}
    }
    response {
        status NOT_FOUND()
        headers {}
        body([
            message: "unknownAdminKey",
            origin: "TallySheetNotAvailableException"
        ])
    }
}
