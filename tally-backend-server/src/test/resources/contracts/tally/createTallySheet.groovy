package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name("createTallySheet")
    request {
        method POST()
        url $(consumer(~/\/public\/[a-z]+/), producer('/name'))
        headers {}
    }
    response {
        status CREATED()
        body([
            id: $(
                consumer('5c9dc2ce8691ff4f8c1b2d54'),
                producer(regex('[a-zA-Z0-9]+'))
            ),
            name: 'name',
            publicKey: $(
                consumer('12345678'), 
                producer(regex('[a-zA-Z0-9]{8}'))
            ),
            adminKey: $(
                consumer('5d1f9fef-e0dc-4f3d-a7e4-72d2220dd827'), 
                producer(regex('[0-9a-zA-z]{8}(-[0-9a-zA-z]{4}){3}-[0-9a-zA-z]{12}'))
            ),
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
