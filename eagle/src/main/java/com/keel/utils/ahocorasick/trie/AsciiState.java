/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/10/31 21:24</create-date>
 *
 * <copyright file="AsciiState.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.keel.utils.ahocorasick.trie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 单字节字符优化版AC自动机节点
 */
public class AsciiState extends State
{
    static final int SIZE = 256;
    /**
     * goto 表，也称转移函数。根据字符串的下一个字符转移到下一个状态
     */
    private State[] success = new State[SIZE];

    /**
     * 转移到下一个状态
     * @param character 希望按此字符转移
     * @param ignoreRootState 是否忽略根节点，如果是根节点自己调用则应该是true，否则为false
     * @return 转移结果
     */
    private State nextState(Character character, boolean ignoreRootState)
    {
        State nextState = this.success[character & 0xff];
        if (!ignoreRootState && nextState == null && this.rootState != null)
        {
            nextState = this.rootState;
        }
        return nextState;
    }

    /**
     * @see org.ahocorasick.trie.AsciiState
     */
    public AsciiState()
    {
    }

    public AsciiState(int depth)
    {
        super(depth);
    }

    /**
     * 转移到下一个状态（基于success转移）
     * @param character 希望按此字符转移
     * @return 转移结果
     */
    public State nextState(Character character)
    {
        return nextState(character, false);
    }

    /**
     * 转移到下一个状态，忽略根节点
     * @param character
     * @return
     */
    public State nextStateIgnoreRootState(Character character)
    {
        return nextState(character, true);
    }

    /**
     * 添加一个状态到success函数
     * @param character
     * @return
     */
    public State addState(Character character)
    {
        State nextState = nextStateIgnoreRootState(character);
        if (nextState == null)
        {
            nextState = new AsciiState(this.depth + 1);
            this.success[character] = nextState;
        }
        return nextState;
    }

    /**
     * 获取success状态
     * @return
     */
    public Collection<State> getStates()
    {
        List<State> stateList = new ArrayList<State>(SIZE);
        for (State state : success)
        {
            if (state != null) stateList.add(state);
        }
        return stateList;
    }

    /**
     * 获取要转移到下一个状态的可能char
     * @return
     */
    public Collection<Character> getTransitions()
    {
        List<Character> stateList = new ArrayList<Character>(SIZE);
        int i = 0;
        for (State state : success)
        {
            if (state != null) stateList.add((char) i);
            ++i;
        }
        return stateList;
    }
}
