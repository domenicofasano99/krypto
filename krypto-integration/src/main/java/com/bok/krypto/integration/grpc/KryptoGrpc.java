package com.bok.krypto.integration.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: krypto.proto")
public final class KryptoGrpc {

  private KryptoGrpc() {}

  public static final String SERVICE_NAME = "Krypto";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bok.krypto.integration.grpc.AccountCreationRequest,
      com.bok.krypto.integration.grpc.AccountCreationResponse> getCreateAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateAccount",
      requestType = com.bok.krypto.integration.grpc.AccountCreationRequest.class,
      responseType = com.bok.krypto.integration.grpc.AccountCreationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bok.krypto.integration.grpc.AccountCreationRequest,
      com.bok.krypto.integration.grpc.AccountCreationResponse> getCreateAccountMethod() {
    io.grpc.MethodDescriptor<com.bok.krypto.integration.grpc.AccountCreationRequest, com.bok.krypto.integration.grpc.AccountCreationResponse> getCreateAccountMethod;
    if ((getCreateAccountMethod = KryptoGrpc.getCreateAccountMethod) == null) {
      synchronized (KryptoGrpc.class) {
        if ((getCreateAccountMethod = KryptoGrpc.getCreateAccountMethod) == null) {
          KryptoGrpc.getCreateAccountMethod = getCreateAccountMethod = 
              io.grpc.MethodDescriptor.<com.bok.krypto.integration.grpc.AccountCreationRequest, com.bok.krypto.integration.grpc.AccountCreationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Krypto", "CreateAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bok.krypto.integration.grpc.AccountCreationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bok.krypto.integration.grpc.AccountCreationResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new KryptoMethodDescriptorSupplier("CreateAccount"))
                  .build();
          }
        }
     }
     return getCreateAccountMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static KryptoStub newStub(io.grpc.Channel channel) {
    return new KryptoStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static KryptoBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new KryptoBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static KryptoFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new KryptoFutureStub(channel);
  }

  /**
   */
  public static abstract class KryptoImplBase implements io.grpc.BindableService {

    /**
     */
    public void createAccount(com.bok.krypto.integration.grpc.AccountCreationRequest request,
        io.grpc.stub.StreamObserver<com.bok.krypto.integration.grpc.AccountCreationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateAccountMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCreateAccountMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bok.krypto.integration.grpc.AccountCreationRequest,
                com.bok.krypto.integration.grpc.AccountCreationResponse>(
                  this, METHODID_CREATE_ACCOUNT)))
          .build();
    }
  }

  /**
   */
  public static final class KryptoStub extends io.grpc.stub.AbstractStub<KryptoStub> {
    private KryptoStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KryptoStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KryptoStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KryptoStub(channel, callOptions);
    }

    /**
     */
    public void createAccount(com.bok.krypto.integration.grpc.AccountCreationRequest request,
        io.grpc.stub.StreamObserver<com.bok.krypto.integration.grpc.AccountCreationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateAccountMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class KryptoBlockingStub extends io.grpc.stub.AbstractStub<KryptoBlockingStub> {
    private KryptoBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KryptoBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KryptoBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KryptoBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bok.krypto.integration.grpc.AccountCreationResponse createAccount(com.bok.krypto.integration.grpc.AccountCreationRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateAccountMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class KryptoFutureStub extends io.grpc.stub.AbstractStub<KryptoFutureStub> {
    private KryptoFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KryptoFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KryptoFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KryptoFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bok.krypto.integration.grpc.AccountCreationResponse> createAccount(
        com.bok.krypto.integration.grpc.AccountCreationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateAccountMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_ACCOUNT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final KryptoImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(KryptoImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_ACCOUNT:
          serviceImpl.createAccount((com.bok.krypto.integration.grpc.AccountCreationRequest) request,
              (io.grpc.stub.StreamObserver<com.bok.krypto.integration.grpc.AccountCreationResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class KryptoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    KryptoBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bok.krypto.integration.grpc.KryptoProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Krypto");
    }
  }

  private static final class KryptoFileDescriptorSupplier
      extends KryptoBaseDescriptorSupplier {
    KryptoFileDescriptorSupplier() {}
  }

  private static final class KryptoMethodDescriptorSupplier
      extends KryptoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    KryptoMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (KryptoGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new KryptoFileDescriptorSupplier())
              .addMethod(getCreateAccountMethod())
              .build();
        }
      }
    }
    return result;
  }
}
