object Deps {

    val dataStore by lazy { "androidx.datastore:datastore:${Versions.DataStore.version}" }

    object Protobuf {

        val kotlin by lazy { "com.google.protobuf:protobuf-kotlin:${Versions.Protobuf.version}" }

        val protoc by lazy { "com.google.protobuf:protoc:${Versions.Protobuf.version}" }
    }
}
