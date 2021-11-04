export class IdService {
  private nextId = 0;
  public getNextId(): string {
    return 'id' + this.nextId++;
  }
  public getNextNumber(): number {
    return this.nextId++;
  }
}

export const idService = new IdService();
