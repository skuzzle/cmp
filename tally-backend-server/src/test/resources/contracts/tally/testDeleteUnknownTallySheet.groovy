package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("deleteUnknownTallySheet")
    request {
        method DELETE()
        url '/admin/unknownAdminKey'
        headers {}
    }
    response {
        status NOT_FOUND()
        headers {}
        body([
                message: "unknownAdminKey",
                origin: "de.skuzzle.tally.service.TallySheetNotAvailableException"
        ])
    }
}
