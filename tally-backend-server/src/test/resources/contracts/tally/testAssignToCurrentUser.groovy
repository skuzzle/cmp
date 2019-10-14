package contracts.tally

org.springframework.cloud.contract.spec.Contract.make {
    name("assignToCurrentUser")
    request {
        method POST()
        url '/adminKey3/assignToCurrentUser'
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
    }
}
