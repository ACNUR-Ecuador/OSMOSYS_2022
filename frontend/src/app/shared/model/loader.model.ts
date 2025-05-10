export interface Loader {
  id: string;
  status: boolean;
  progress?: number;
  statusText?: string;
}
