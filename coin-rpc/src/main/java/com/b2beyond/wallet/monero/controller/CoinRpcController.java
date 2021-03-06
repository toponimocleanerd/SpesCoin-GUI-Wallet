package com.speseyond.wallet.monero.controller;

import com.speseyond.wallet.rpc.model.coin.BlockCount;
import com.speseyond.wallet.rpc.model.coin.BlockWrapper;
import com.speseyond.wallet.rpc.model.coin.TransactionWrapper;
import com.speseyond.wallet.rpc.CoinController;
import com.speseyond.wallet.rpc.JsonRpcExecutor;
import com.speseyond.wallet.rpc.RpcPoller;

import java.util.ArrayList;
import java.util.List;

public class CoinRpcController implements CoinController {

	private JsonRpcExecutor<BlockCount> blockCountExecutor;
	private JsonRpcExecutor<BlockWrapper> blockWrapperExecutor;
	private JsonRpcExecutor<TransactionWrapper> transactionWrapperExecutor;

	private List<RpcPoller<?>> pollers = new ArrayList<>();

	public CoinRpcController(String baseUrl) {
		blockCountExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getblockcount", BlockCount.class);
		blockWrapperExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getlastblockheader", BlockWrapper.class);
		transactionWrapperExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "f_transaction_json", TransactionWrapper.class);
	}

	public void addPollers(RpcPoller poller) {
		pollers.add(poller);
		new Thread(poller).start();
	}

	public void stop() {
		for (RpcPoller poller : pollers) {
			poller.stop();
		}
	}

	public void restart() {
		for (RpcPoller poller : pollers) {
			poller.start();
			new Thread(poller).start();
		}
	}

	public JsonRpcExecutor<BlockCount> getBlockCountExecutor() {
		return blockCountExecutor;
	}

	public JsonRpcExecutor<BlockWrapper> getBlockWrapperExecutor() {
		return blockWrapperExecutor;
	}

	public JsonRpcExecutor<TransactionWrapper> getTransactionWrapperExecutor() {
		throw new NoSuchMethodError();
	}

}
