
package com.bugjc.ea.code.generator.core.keywords;

import com.bugjc.ea.code.generator.config.IKeyWordsHandler;

import java.util.List;
import java.util.Locale;

/**
 * 基类关键字处理
 *
 * @author nieqiurong 2020/5/8.
 * @since 3.3.2
 */
public abstract class BaseKeyWordsHandler implements IKeyWordsHandler {
    
    private List<String> keyWords;
    
    public BaseKeyWordsHandler(List<String> keyWords) {
        this.keyWords = keyWords;
    }
    
    @Override
    public List<String> getKeyWords() {
        return keyWords;
    }
    
    @Override
    public boolean isKeyWords(String columnName) {
        return getKeyWords().contains(columnName.toUpperCase(Locale.ENGLISH));
    }
}
