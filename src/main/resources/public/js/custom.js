function highlight(jsonString) {
	var chaine = jsonString;
	let keywords = ['"id"', '"error"'];
	for (let keyword of keywords) {
		var regexp = new RegExp(keyword,"g");
		chaine = chaine.replace(regexp, '<span class="highlighted">' + keyword + '</span>');
	}
	return chaine;	
}
