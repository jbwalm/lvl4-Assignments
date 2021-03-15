import itertools

file1 = open("./fulltext.txt", "r")
text = file1.read()
file1.close()


def get_permutation(permutaton_length):
	blank_perm = []
	for i in range(permutaton_length):
		blank_perm.append(i)
	
	return blank_perm


def try_permutation(try_this):
	key_len = len(try_this)
	text_len = len(text)
	cipher = [""] * key_len
	best_pairs = []
	best_pair_scores = []
	
	for index, item in enumerate(cipher):
		pointer = index
		while (pointer < text_len):
			item += text[pointer]
			pointer += key_len
		cipher[index] = item


	for index, item in enumerate(cipher):
		best_pair_score = 0
		best_pair = []
		for index2, item2 in enumerate(cipher):
			pair_score = 0
			pointer = 0
			while (pointer < len(item) and pointer < len(item2)):
				concat = item[pointer] + item2[pointer]
				if (concat.lower() == 'th' or concat.lower == 'he'):
					pair_score += 1
				pointer += 1
			if (pair_score > best_pair_score):
				best_pair_score = pair_score
				best_pair = (index, index2, best_pair_score)
		
		best_pairs.append(best_pair)
		best_pair_scores.append(best_pair_score)
			
	return best_pairs, best_pair_scores

for i in range(10, 21):
	permutation = get_permutation(i)
	print('-------\n', permutation)
	perm_pairs, pair_scores = try_permutation(permutation)
	print(perm_pairs)
	print('\n-------')