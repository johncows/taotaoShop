package com.kk.MQ;

import com.kk.mapper.SearchMapper;
import com.kk.pojo.SearchItem;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

    @Autowired
    private SearchMapper searchMapper;

    @Autowired
    @Qualifier("cloudSolrServer")
    private SolrServer solrServer;


    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            long id = Long.parseLong(text);
            SearchItem searchItem= searchMapper.getSearchItemById(id);
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            solrServer.add(document);
            solrServer.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
