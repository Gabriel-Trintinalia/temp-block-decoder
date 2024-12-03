package net.consensys.linea;

import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.ethereum.core.Block;
import org.hyperledger.besu.ethereum.mainnet.MainnetBlockHeaderFunctions;
import org.hyperledger.besu.ethereum.rlp.RLP;

public class Main {
    public static void main(String[] args) {
      String decompressedBlockRlpDecoded =
        "f90783f901f1a00000000000000000000000000000000000000000000000000000000000000000a01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347940000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000000a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421b9010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000808080808466f5b8d580a00000000000000000000000000000000000000000000000000000000000000000880000000000000000f9058bb9012a02f9012680825d74841dcd6500841dcd650e83044d92943921e8cb45b17fc029a0a6de958330ca4e58339080b8e4bc6511880000000000000000000000003aab2285ddcddad8edf438c1bab47e1a9d05a9b4000000000000000000000000e5d7c2a44ffddf6b295a15c148167daaaf5cf34f00000000000000000000000098f0f120de21a90f220b0027a9c70029df9bbde40000000000000000000000000000000000000000000000000000000066f5b9ff0000000000000000000000000000000000000000000000000000000000bb4e2e00000000000000000000000000000000000000000000000029bff9d8cf6738000000000000000000000000000000000000000000000000000000000000000000c0809498f0f120de21a90f220b0027a9c70029df9bbde480b9012902f9012580829ee8841dcd6500841dcd650e83044daa943921e8cb45b17fc029a0a6de958330ca4e58339080b8e4bc6511880000000000000000000000003aab2285ddcddad8edf438c1bab47e1a9d05a9b4000000000000000000000000e5d7c2a44ffddf6b295a15c148167daaaf5cf34f000000000000000000000000004df5de266316d7aa68a639ad73d795a631e2e60000000000000000000000000000000000000000000000000000000066f5b9ff0000000000000000000000000000000000000000000000000000000000ec380e00000000000000000000000000000000000000000000000034a6ad47497df2000000000000000000000000000000000000000000000000000000000000000000c080934df5de266316d7aa68a639ad73d795a631e2e680b86602f863804b8409ece7408409ece747830369d894ad7f33984bed10518012013d4ab0458d37fee6f380a4a0712d6800000000000000000000000000000000000000000000000000001c31bffcf000c08094a47a0b73b7edad29fd0185a8c38791d73ce66f5c80f901640984092dda8082e02d94c626845bf4e6a5802ef774da0b3dfc6707f015f78705af3107a40000b90124fc18063800000000000000000000000000000000000000000000000000000000000000c000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005e809a85aa182a9921edd10a4163745bb3e362840000000000000000000000000000000000000000000000000005af3107a4000000000000000000000000000000000000000000000000000000000000000000310000000000000000000000000000000000000000000000000000000000016082000000000000000000000000000000000000000000000000000000000000002a307845323331343930373546353830423936463838426144313536363435303361333533316241373141000000000000000000000000000000000000000000008094e23149075f580b96f88bad15664503a3531ba71a80f9016082018084091e9840830450f294610d2f07b7edc67565160f587f37636194c34e7480b9012418a13086000000000000000000000000000000000000000000000008798aa85e9f5c836500000000000000000000000000000000000000000000000000080405f9261e0d00000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000315fff7c53d75737d5a9f5165bed76ca1f689c730000000000000000000000000000000000000000000000000000000066f5bd9700000000000000000000000000000000000000000000000000000000000000010000000000000000000000001a51b19ce03dbe0cb44c1528e34a7edd7771e9af000000000000000000000000e5d7c2a44ffddf6b295a15c148167daaaf5cf34f00000000000000000000000000000000000000000000000000000000000000008094315fff7c53d75737d5a9f5165bed76ca1f689c7380c0";
      var rlpInput = RLP.input(Bytes.fromHexString(decompressedBlockRlpDecoded));


      Block block =  new BlockDecoder().readFrom(rlpInput, new MainnetBlockHeaderFunctions());
      System.out.println(block);
    }
}