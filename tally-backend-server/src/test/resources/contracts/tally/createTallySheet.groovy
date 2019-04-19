package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name("createTallySheet")
    request {
        method POST()
        url $(consumer(~/\/[a-zA-Z0-9]+/), producer('/name'))
        headers {}
    }
    response {
        status CREATED()
        body([
            id: $(
                consumer('5c9dc2ce8691ff4f8c1b2d54'),
                producer(regex('[a-zA-Z0-9]+'))
            ),
            name: fromRequest().path(0),
            publicKey: 'publicKey',
            adminKey: 'adminKey',
            createDateUTC: $(
                consumer('1987-09-12T11:11:00Z'),
                producer(regex(iso8601WithOffset()))
            ),
            lastModifiedDateUTC: $(
                consumer('1987-09-12T11:11:00Z'),
                producer(regex(iso8601WithOffset()))
            ),
            increments: []
        ])
        headers {
            contentType(applicationJson())
        }
    }
}
