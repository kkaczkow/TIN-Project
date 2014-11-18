#ifndef AGENTREPO3000_VECTOR_HPP
#define AGENTREPO3000_VECTOR_HPP

#include <vector>
#include <algorithm>
#include <iterator>
#include <initializer_list>

namespace AgentRepo3000 {

template <typename T>
class vector {
public:
	class const_iterator : public virtual std::vector<T>::const_iterator {
		const vector<T>* v;
	public:
		const_iterator(const vector<T>* v, typename std::vector<T>::const_iterator iter) : std::vector<T>::const_iterator(iter), v(v) {}
		~const_iterator() {
			v->iterators.erase(std::remove(v->iterators.begin(), v->iterators.end(), this));
		}
	};
	class iterator : public virtual const_iterator, public virtual std::vector<T>::iterator {
	public:
		iterator(const vector<T>* v, typename std::vector<T>::iterator iter) : const_iterator(v, iter), std::vector<T>::iterator(iter) {}
	};
	friend class const_iterator;
	friend class iterator;
private:
	std::vector<T> data;
	mutable std::vector<const_iterator*> iterators;
public:
	vector(std::size_t N, T t = T()) : data(N, t) {}
	vector(std::initializer_list<T> list) : data{list} {}
	iterator begin() {
		return {this, data.begin()};
	}
	const_iterator begin() const {
		return {this, data.begin()};
	}
	const_iterator cbegin() const {
		return {this, data.begin()};
	}
	iterator end() {
		return {this, data.end()};
	}
	const_iterator end() const {
		return {this, data.end()};
	}
	const_iterator cend() const {
		return {this, data.end()};
	}
	iterator erase(const_iterator pos) {
		auto next = data.erase(pos);
		for (auto&& iter : iterators)
			if (iter.iterator == pos)
				iter.iterator = next;
	}
	iterator erase(const_iterator first, const_iterator last) {
		for (auto it = first; it != last; ++it)
			erase(it);
	}
	void push_back(const T& t) { data.push_back(t);  }
	void push_back(T&& t) { data.push_back(std::forward<T>(t)); }
	template <typename... Args>
	void emplace_back(Args&&... args) { data.emplace_back(std::forward<Args>(args)...); }
};

}

#endif /* AGENTREPO3000_VECTOR_HPP */
