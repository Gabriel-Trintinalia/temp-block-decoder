package net.consensys.linea;

import java.util.List;
import java.util.Optional;

import org.hyperledger.besu.ethereum.core.Block;
import org.hyperledger.besu.ethereum.core.BlockBody;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.BlockHeaderFunctions;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.hyperledger.besu.ethereum.core.Withdrawal;
import org.hyperledger.besu.ethereum.rlp.RLPInput;

public class BlockDecoder {

  NoSignatureTransactionDecoder transactionDecoder = new NoSignatureTransactionDecoder();

  public Block readFrom(final RLPInput in, final BlockHeaderFunctions hashFunction) {
    in.enterList();

    // Read the header
    final BlockHeader header = BlockHeader.readFrom(in, hashFunction);

    // Use NoSignatureTransactionDecoder to decode transactions
    final List<Transaction> transactions = in.readList( transactionDecoder::decode);

    // Read the ommers
    final List<BlockHeader> ommers = in.readList(rlp -> BlockHeader.readFrom(rlp, hashFunction));

    // Read the withdrawals
    final Optional<List<Withdrawal>> ignored =
      in.isEndOfCurrentList() ? Optional.empty() : Optional.of(in.readList(Withdrawal::readFrom));

    in.leaveList();
    return new Block(header, new BlockBody(transactions, ommers));
  }
}
