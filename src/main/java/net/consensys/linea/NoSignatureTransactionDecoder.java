/*
 * Copyright ConsenSys 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package net.consensys.linea;

import java.math.BigInteger;

import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.SECPSignature;
import org.hyperledger.besu.datatypes.AccessListEntry;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.hyperledger.besu.ethereum.core.encoding.registry.TransactionDecoder;
import org.hyperledger.besu.ethereum.rlp.RLP;
import org.hyperledger.besu.ethereum.rlp.RLPInput;

public class NoSignatureTransactionDecoder implements TransactionDecoder {
  @Override
  public Transaction decode(final RLPInput input) {
    {
      if (!input.nextIsList()) {
        final Bytes typedTransactionBytes = input.readBytes();
        final RLPInput transactionInput = RLP.input(typedTransactionBytes.slice(1));
        byte transactionType = typedTransactionBytes.get(0);
        if (transactionType == 0x01) {
          return decodeAccessList(transactionInput);
        }
        if (transactionType == 0x02) {
          return decode1559(transactionInput);
        }
        throw new IllegalArgumentException("Unsupported transaction type");
      } else { // Frontier transaction
        return decodeFrontier(input);
      }
    }
  }

  private Transaction decodeAccessList(final RLPInput transactionInput) {
    final Transaction.Builder builder = Transaction.builder();

    transactionInput.enterList();
    builder
        .type(TransactionType.ACCESS_LIST)
        .chainId(BigInteger.valueOf(transactionInput.readLongScalar()))
        .nonce(transactionInput.readLongScalar())
        .gasPrice(Wei.of(transactionInput.readUInt256Scalar()))
        .gasLimit(transactionInput.readLongScalar())
        .to(
            transactionInput.readBytes(
                addressBytes -> addressBytes.isEmpty() ? null : Address.wrap(addressBytes)))
        .value(Wei.of(transactionInput.readUInt256Scalar()))
        .payload(transactionInput.readBytes())
        .accessList(
            transactionInput.readList(
                accessListEntryRLPInput -> {
                  accessListEntryRLPInput.enterList();
                  final AccessListEntry accessListEntry =
                      new AccessListEntry(
                          Address.wrap(accessListEntryRLPInput.readBytes()),
                          accessListEntryRLPInput.readList(RLPInput::readBytes32));
                  accessListEntryRLPInput.leaveList();
                  return accessListEntry;
                }));
    transactionInput.readUnsignedByteScalar();
    builder.sender(Address.extract(transactionInput.readUInt256Scalar()));
    transactionInput.readUInt256Scalar();
    transactionInput.leaveList();
    return builder.signature(new SECPSignature(BigInteger.ZERO, BigInteger.ZERO, (byte) 0)).build();
  }

  private Transaction decode1559(final RLPInput transactionInput) {
    final Transaction.Builder builder = Transaction.builder();
    transactionInput.enterList();
    final BigInteger chainId = transactionInput.readBigIntegerScalar();
    builder
        .type(TransactionType.EIP1559)
        .chainId(chainId)
        .nonce(transactionInput.readLongScalar())
        .maxPriorityFeePerGas(Wei.of(transactionInput.readUInt256Scalar()))
        .maxFeePerGas(Wei.of(transactionInput.readUInt256Scalar()))
        .gasLimit(transactionInput.readLongScalar())
        .to(transactionInput.readBytes(v -> v.isEmpty() ? null : Address.wrap(v)))
        .value(Wei.of(transactionInput.readUInt256Scalar()))
        .payload(transactionInput.readBytes())
        .accessList(
            transactionInput.readList(
                accessListEntryRLPInput -> {
                  accessListEntryRLPInput.enterList();
                  final AccessListEntry accessListEntry =
                      new AccessListEntry(
                          Address.wrap(accessListEntryRLPInput.readBytes()),
                          accessListEntryRLPInput.readList(RLPInput::readBytes32));
                  accessListEntryRLPInput.leaveList();
                  return accessListEntry;
                }));
    transactionInput.readUnsignedByteScalar();
    builder.sender(Address.extract(transactionInput.readUInt256Scalar()));
    transactionInput.readUInt256Scalar();
    transactionInput.leaveList();
    return builder.signature(new SECPSignature(BigInteger.ZERO, BigInteger.ZERO, (byte) 0)).build();
  }

  private Transaction decodeFrontier(final RLPInput input) {
    final Transaction.Builder builder = Transaction.builder();
    input.enterList();
    builder
        .type(TransactionType.FRONTIER)
        .nonce(input.readLongScalar())
        .gasPrice(Wei.of(input.readUInt256Scalar()))
        .gasLimit(input.readLongScalar())
        .to(input.readBytes(v -> v.isEmpty() ? null : Address.wrap(v)))
        .value(Wei.of(input.readUInt256Scalar()))
        .payload(input.readBytes());

    input.readBigIntegerScalar();
    builder.sender(Address.extract(input.readUInt256Scalar()));
    input.readUInt256Scalar();
    final SECPSignature signature = new SECPSignature(BigInteger.ZERO, BigInteger.ZERO, (byte) 0);
    input.leaveList();
    return builder.signature(signature).build();
  }
}
