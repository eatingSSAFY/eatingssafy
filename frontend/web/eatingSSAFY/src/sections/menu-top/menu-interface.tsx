interface foodEach {
  content: string,
  allergyList: string[]
}

export interface myMenus {
  category: string,
  foodList: foodEach[],
  servingAt: string,
}

export interface amounts {
  category: string,
  servedAmountPerMin: number, // 분당 소진되는 속도
  stock: number,  // 초기 재고
  value: number, // 현재 남은 수량
  velocity: number,
}

export interface valueProps {
  value: number,
  stock: number,
}

export interface preferences {
  servingAt : string,
  category : string,
  foodId : number,
  likeCnt : number,
  dislikeCnt : number,
  preferenceId : number | null,
  like : number | null,
}