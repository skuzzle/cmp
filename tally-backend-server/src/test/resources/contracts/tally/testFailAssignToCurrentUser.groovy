package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("failToAssignToCurrentUser")
    request {
        method POST()
        url '/adminKey2/assignToCurrentUser'
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status BAD_REQUEST()
        body([
            message: "Sheet with name 'existing2' is already assigned to: test:user1",
            origin: "de.skuzzle.cmp.service.UserAssignmentException"
        ])
    }
}
