// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: krypto.proto

package com.bok.krypto.integration.grpc;

public interface AccountCreationResponseOrBuilder extends
        // @@protoc_insertion_point(interface_extends:AccountCreationResponse)
        com.google.protobuf.MessageOrBuilder {

    /**
     * <code>bool created = 1;</code>
     *
     * @return The created.
     */
    boolean getCreated();

    /**
     * <code>string error = 2;</code>
     *
     * @return The error.
     */
    java.lang.String getError();

    /**
     * <code>string error = 2;</code>
     *
     * @return The bytes for error.
     */
    com.google.protobuf.ByteString
    getErrorBytes();
}