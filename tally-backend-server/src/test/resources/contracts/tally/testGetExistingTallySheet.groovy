package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name("getExistingTallySheet")
    request {
        method GET()
        url $(consumer(~/\/public\/[a-z]+/), producer(execute('existingTallySheetPublicUri()')))
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
            publicKey: $(
                consumer('12345678'), 
                producer(regex('[a-zA-Z0-9]{8}'))
            ),
            adminKey: null,
            createDate: $(
                consumer('1987-12-09T11:11:00'),
                producer(regex('\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+'))
            ),
            lastModifiedDate: $(
                consumer('1987-12-09T11:11:00'),
                producer(regex('\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+'))
            ),
            increments: []
        ])
        headers {
            contentType(applicationJson())
        }
    }
}
