package com.bano.goblin.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * Provides a accordion implementation
 */
public abstract class BaseAccordionAdapter<T, E extends ViewDataBinding> extends BaseAdapter<AccordionContainer<T>, E>{

    private final boolean mExpandOnlyOneFlag;
    private AccordionContainer<T> mPreviousAccordionOpened;

    public abstract void onCategoryBindViewHolder(E e, String category);
    public abstract void onItemBindViewHolder(E e, T t);

    public BaseAccordionAdapter(List<AccordionContainer<T>> items,
                                @LayoutRes int categoryLayoutRes, boolean expandOnlyOne,
                                OnClickListener<AccordionContainer<T>> listener){
        super(items, categoryLayoutRes, listener);
        mExpandOnlyOneFlag = expandOnlyOne;
    }

    @Override
    public BaseAdapter.ViewHolder<AccordionContainer<T>, E> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        E binding = DataBindingUtil.inflate(layoutInflater, getLayoutRes(), parent, false);
        return new BaseAdapter.ViewHolder<>(binding, new OnClickListener<AccordionContainer<T>>() {
            @Override
            public void onClicked(AccordionContainer<T> accordionContainer) {
                if(accordionContainer.type == AccordionContainer.TYPE_CATEGORY) {
                    if(accordionContainer.isOpen()){
                        collapse(accordionContainer);
                    }
                    else {
                        if(mExpandOnlyOneFlag && mPreviousAccordionOpened != null){
                            collapse(mPreviousAccordionOpened);
                        }
                        expand(accordionContainer);
                    }
                }
                else getListener().onClicked(accordionContainer);
            }
        });
    }

    private void expand(AccordionContainer<T> accordionContainer){
        mPreviousAccordionOpened = accordionContainer;
        accordionContainer.setOpen(true);
        int position = accordionContainer.getPosition() + 1;
        for (T t : accordionContainer.tList) {
            addItem(position, new AccordionContainer<>(t));
            ++position;
        }

        for(int i = position; i < getItems().size(); i++){
            getItems().get(i).setPosition(i);
        }
    }

    private void collapse(AccordionContainer<T> accordionContainer){
        accordionContainer.setOpen(false);
        for (T t : accordionContainer.tList) {
            remove(new AccordionContainer<>(t));
        }

        for(int i = accordionContainer.getPosition() + 1; i < getItems().size(); i++){
            getItems().get(i).setPosition(i);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder<AccordionContainer<T>, E> holder, int position) {
        AccordionContainer<T> accordionContainer = getItems().get(position);
        accordionContainer.setPosition(position);
        holder.binding.getRoot().setTag(accordionContainer);
        switch (accordionContainer.type){
            case AccordionContainer.TYPE_CATEGORY:
                this.onCategoryBindViewHolder(holder.binding, accordionContainer.category);
                break;
            case AccordionContainer.TYPE_ITEM:
                this.onItemBindViewHolder(holder.binding, accordionContainer.t);
                break;
        }
    }

    @Override
    protected void onBindViewHolder(E e, AccordionContainer<T> accordionContainer) {

    }
}
