package hust.tools.csc.checker;

import java.util.ArrayList;
import java.util.HashMap;

import hust.tools.csc.correct.Correction;
import hust.tools.csc.correct.Corrector;
import hust.tools.csc.correct.Suggestions;
import hust.tools.csc.detecet.Detector;
import hust.tools.csc.detecet.SpellError;
import hust.tools.csc.util.Sentence;

public class CheckResult implements Corrector, Detector {
	
	/**
	 * 错误位置的推荐字
	 */
	private HashMap<Integer, Suggestions> suggestion;
	
	/**
	 * 候选纠正句中的纠正字
	 */
	private ArrayList<Correction[]> correctionList;
	
	/**
	 * 拼写错误列表
	 */
	private ArrayList<SpellError[]> errorList;
		
	public CheckResult() {
		suggestion = new HashMap<Integer, Suggestions>();
		correctionList = new ArrayList<Correction[]>();
		errorList = new ArrayList<SpellError[]>();
	}
	
	public CheckResult(Sentence sentence, ArrayList<Sentence> candidates) {
		suggestion = new HashMap<Integer, Suggestions>();
		correctionList = new ArrayList<Correction[]>();
		errorList = new ArrayList<SpellError[]>();
		
		build(sentence, candidates);
	}
	
	private void build(Sentence sentence, ArrayList<Sentence> candSentences) {
		if(candSentences != null) {
			for(Sentence candSentence : candSentences) {
				int len = candSentence.size();
				if(len != sentence.size())
					continue;
				
				ArrayList<SpellError> errors = new ArrayList<>();
				for(int i = 0; i < sentence.size(); i++) {
					String character = candSentence.getToken(i);
					if(!sentence.getToken(i).equals(character)) 
						errors.add(new SpellError(character, i));
				}//end for
				
				if(errors.size() != 0)
					addSpellErrors(errors.toArray(new SpellError[errors.size()]));
				else
					addSpellErrors(null);
				
				ArrayList<Correction> corrections = new ArrayList<>();
				for(int i = 0; i < len; i++) {
					if(candSentence.getToken(i).equals(sentence.getToken(i)))
						corrections.add(new Correction(candSentence.getToken(i), i));
				}
				
				if(corrections.size() != 0)
					addCorrections(corrections.toArray(new Correction[corrections.size()]));
				else
					addCorrections(null);
			}
		}
	}

	@Override
	public int[] getErrorLocation(int n) {
		SpellError[] errors = getErrors(n);
		if(errors == null)
			return null;

		int size = errors.length;
		int[] errorLoactions = new int[size];
		
		for(int i = 0; i < size; i++)
			errorLoactions[i] = errors[i].getLocation();
		
		return errorLoactions;		
	}

	@Override
	public String[] getErrorCharacter(int n) {
		SpellError[] errors = getErrors(n);
		if(errors == null)
			return null;

		int size = errors.length;
		String[] errorLoactions = new String[size];
		
		for(int i = 0; i < size; i++)
			errorLoactions[i] = errors[i].getCharacter();
		
		return errorLoactions;		
	}
	
	@Override
	public int[][] getErrorLocation() {
		int[][] res = new int[candiateCounts()][];
		
		for(int i = 0; i < res.length; i++) {
			res[i] = getErrorLocation(i);
		}
		return res;
	}
	
	@Override
	public String[][] getErrorCharacter() {
		String[][] errors =  new String[candiateCounts()][];
		
		for(int i = 0; i < errors.length; i++) {
			errors[i] = getErrorCharacter(i);
		}
		
		return errors;
	}
	
	@Override
	public SpellError[] getErrors(int index) {
		if(index >= 0 && index < errorList.size())
			return errorList.get(index);
		
		return null;
	}

	@Override
	public Correction[][] getCorrects() {
		if(correctionList.size() == 0)
			return null;
		
		Correction[][] corrections = new Correction[correctionList.size()][];
		for(int i = 0; i < correctionList.size(); i++)
			corrections[i] = correctionList.get(0);
		return corrections;
	}

	@Override
	public Correction[] getCorrects(int order) {
		if(order > correctionList.size() || order < 0)
			return null;
		
		return correctionList.get(order);
	}

	@Override
	public Suggestions getSuggestions(int location) {
		if(suggestion.containsKey(location))
			return suggestion.get(location);
		
		return null;
	}
	
	/**
	 * 判断是否含有给定位置的纠正词
	 * @param location	待判断的位置
	 * @return			true-是/false-否
	 */
	public boolean contains(int location) {
		return suggestion.containsKey(location);
	}
	
	/**
	 * 添加一个候选纠正句的信息（所有纠正的字）
	 * @param corrections	纠正的字组
	 */
	public void addCorrections(Correction[] corrections) {
		correctionList.add(corrections);
		
		for(Correction correction : corrections) {
			int location = correction.getLocation();
			String character = correction.getCharacter();
			
			if(suggestion.containsKey(location)) {
				suggestion.get(location).add(character);
			}else {
				Suggestions suggestions = new Suggestions();
				suggestions.add(character);
				
				suggestion.put(location, suggestions);
			}
		}
	}
	
	/**
	 * 添加一个拼写错误
	 * @param error
	 */
	public void addSpellErrors(SpellError[] error) {
		if(!errorList.contains(error))
			errorList.add(error);
	}

	/**
	 * 返回候选检错的个数
	 * @return	拼写错误的个数
	 */
	public int candiateCounts() {
		return errorList.size();
	}
}
