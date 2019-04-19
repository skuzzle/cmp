package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name("getExistingTallySheet")
    request {
        method GET()
        url '/public/publicKey'
        headers {}
    }
    response {
        status OK()
        body([
            id: $(
                consumer('5c9dc2ce8691ff4f8c1b2d54'),
                producer(regex('[a-zA-Z0-9]+'))
            ),
            name: 'existing',
            publicKey: 'publicKey',
            adminKey: null,
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
